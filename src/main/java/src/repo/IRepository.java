package src.repo;

import src.domain.exception.MyException;
import src.domain.prgstate.PrgState;

import java.util.Vector;

public interface IRepository {
    Vector<PrgState> getPrgList();
    Vector<PrgState> setPrgList(Vector<PrgState> prgList);
    void logPrgStateExec(PrgState prgState) throws MyException;

    void logAllPrgStateExec() throws MyException;
    String getLogFilePath();
}
