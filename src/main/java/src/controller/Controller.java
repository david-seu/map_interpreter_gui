package src.controller;

import src.domain.exception.MyException;
import src.domain.prgstate.PrgState;
import src.domain.value.RefValue;
import src.domain.value.Value;
import src.repo.IRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repo;

    private ExecutorService executor;

    public Controller(IRepository r) {repo=r;}

    public Vector<PrgState> removeCompletedPrg(Vector<PrgState> inPrgList) {
        if(inPrgList.size() == 1 && !inPrgList.get(0).isNotCompleted())
            return inPrgList;
        return inPrgList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toCollection(Vector::new));
    }


    public void oneStepForAllPrg(Vector<PrgState> prgList) throws InterruptedException, MyException {

        for (PrgState prg : prgList) {
                repo.logPrgStateExec(prg);
        }

        repo.logAllPrgStateExec();

        Vector<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toCollection(Vector::new));

        Vector<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(p -> p != null)
                .collect(Collectors.toCollection(Vector::new));

        prgList.addAll(newPrgList);

        for (PrgState prg : prgList) {
                repo.logPrgStateExec(prg);
        }
        repo.logAllPrgStateExec();

        repo.setPrgList(prgList);

    }
    public void allStep() throws MyException, InterruptedException, IOException {
        executor = Executors.newFixedThreadPool(2);
        Vector<PrgState> prgList = removeCompletedPrg(repo.getPrgList());

        while(!prgList.isEmpty()){
            prgList.forEach(prg -> prg.getHeap().setContent(safeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().getContent().values()),
                    prg.getHeap().getContent())));
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdown();
        repo.setPrgList(prgList);
    }

    public void oneStepAll() throws MyException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        Vector<PrgState> prgList = removeCompletedPrg(repo.getPrgList());

        prgList.forEach(prg -> prg.getHeap().setContent(safeGarbageCollector(
                getAddrFromSymTable(prg.getSymTable().getContent().values()),
                prg.getHeap().getContent())));

        oneStepForAllPrg(prgList);

        prgList = removeCompletedPrg(repo.getPrgList());
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }


    private static Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap){
        return heap.entrySet().stream()
                .filter(e -> (symTableAddr.contains(e.getKey()) || isRefInHeap(new RefValue(e.getKey(),e.getValue().getType()), heap)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream().filter(v -> v instanceof RefValue).map(v -> {
            RefValue v1 = (RefValue) v;
            return (Integer) v1.getVal();
        }).collect(Collectors.toList());
    }

    private static boolean isRefInHeap(RefValue ref, Map<Integer, Value> heap){
        for(Integer key: heap.keySet()){
            if(heap.get(key).equals(ref))
                return true;
        }
        return false;
    }

    public Vector<PrgState> getPrgStates() {
        return repo.getPrgList();
    }
}