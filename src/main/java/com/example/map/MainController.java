package com.example.map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import src.controller.Controller;
import src.domain.exception.EmptyStackException;
import src.domain.exception.MyException;
import src.domain.prgstate.*;
import src.domain.stmt.IStmt;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.util.*;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}

public class MainController {
    private Controller controller;

    @FXML
    private TableView<Pair<Integer, Value>> heapTable;

    @FXML
    private TableColumn<Pair<Integer, Value>, Integer> addressColumn;

    @FXML
    private TableColumn<Pair<Integer, Value>, String> valueColumn;

    @FXML
    private ListView<String> outputList;

    @FXML
    private ListView<String> fileList;

    @FXML
    private ListView<Integer> programStateList;

    @FXML
    private ListView<String> executionStackList;

    @FXML
    private TableView<Pair<String, Value>> symbolTable;

    @FXML
    private TableColumn<Pair<String, Value>, String> symVariableColumn;

    @FXML
    private TableColumn<Pair<String, Value>, Value> symValueColumn;


    @FXML
    private TableView<Pair<String, IStmt>> procTable;

    @FXML
    private TableColumn<Pair<String, IStmt>, String> procNameParamColumn;

    @FXML
    private TableColumn<Pair<String, IStmt>, IStmt> procBodyColumn;

    @FXML
    private TextField numberOfProgramStates;

    @FXML
    private Button oneStep;

    @FXML
    public void initialize() {
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        procNameParamColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        procBodyColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().second));
        symVariableColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        symValueColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().second));
        oneStep.setOnAction(actionEvent -> {
            if (controller == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            boolean programStateLeft = Objects.requireNonNull(getCurrentProgramState()).getStack().isEmpty();
            if (programStateLeft) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing left to execute", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            try {
                controller.oneStepAll();
                populate();
            } catch (RuntimeException | MyException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
                controller = null;
            }
        });
        programStateList.setOnMouseClicked(mouseEvent -> {
            try {
                populate();
            } catch (EmptyStackException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private PrgState getCurrentProgramState() {
        if (controller.getPrgStates().isEmpty())
            return null;
        int currentId = programStateList.getSelectionModel().getSelectedIndex();
        if (currentId == -1)
            return controller.getPrgStates().get(0);
        return controller.getPrgStates().get(currentId);
    }

    public void setController(Controller controller) throws EmptyStackException {
        this.controller = controller;
        populate();
    }

    private void populate() throws EmptyStackException {
        populateHeap();
        populateProgramStateIdentifiers();
        populateFileTable();
        populateOutput();
        populateSymbolTable();
        populateExecutionStack();
        populateProcTable();
    }

    private void populateProcTable() {
        List<Pair<String, IStmt>> procTableList = new ArrayList<>();
        for (Map.Entry<String, javafx.util.Pair<ArrayList<Value>, IStmt>> entry : controller.getPrgStates().get(0).getProcTable().getContent().entrySet())
            procTableList.add(new Pair<>(entry.getKey()+" "+entry.getValue().getKey(), entry.getValue().getValue()));
        procTable.setItems(FXCollections.observableList(procTableList));
        procTable.refresh();
    }

    private void populateHeap() {
        MyIDictionary<Integer, Value> heap;
        if (!controller.getPrgStates().isEmpty())
            heap = controller.getPrgStates().get(0).getHeap();
        else heap = new MyHeap();
        List<Pair<Integer, Value>> heapTableList = new ArrayList<>();
        for (Map.Entry<Integer, Value> entry : heap.getContent().entrySet())
            heapTableList.add(new Pair<>(entry.getKey(), entry.getValue()));
        heapTable.setItems(FXCollections.observableList(heapTableList));
        heapTable.refresh();
    }

    private void populateProgramStateIdentifiers() {
        List<PrgState> programStates = controller.getPrgStates();
        List<Integer> idList = new ArrayList<>();
        if (!(programStates.size() == 1 && !programStates.get(0).isNotCompleted())) {
            idList = programStates.stream().map(PrgState::getId).collect(Collectors.toList());
        }
        programStateList.setItems(FXCollections.observableList(idList));
        numberOfProgramStates.setText("" + programStates.size());
    }

    private void populateFileTable() {
        ArrayList<StringValue> files;
        if (!controller.getPrgStates().isEmpty())
            files = new ArrayList<>(controller.getPrgStates().get(0).getFileTable().getKeys());
        else files = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();
        for(StringValue file : files){
            fileNames.add(file.getVal().toString());
        }
        fileList.setItems(FXCollections.observableArrayList(fileNames));
    }

    private void populateOutput() {
        MyIList<Value> output;
        if (!controller.getPrgStates().isEmpty())
            output = controller.getPrgStates().get(0).getOut();
        else output = new MyList<>();
        outputList.setItems(FXCollections.observableList(output.getValues().stream().map(Value::toString).collect(Collectors.toList())));
        outputList.refresh();
    }

    private void populateSymbolTable() throws EmptyStackException {
        PrgState state = getCurrentProgramState();
        List<Pair<String, Value>> symbolTableList = new ArrayList<>();
        if (state != null)
            for (Map.Entry<String, Value> entry : state.getSymTables().top().getContent().entrySet())
                symbolTableList.add(new Pair<>(entry.getKey(), entry.getValue()));
        symbolTable.setItems(FXCollections.observableList(symbolTableList));
        symbolTable.refresh();
    }

    private void populateExecutionStack() {
        PrgState state = getCurrentProgramState();
        List<String> executionStackListAsString = new ArrayList<>();
        if (state != null)
            for (IStmt s : state.getStack().getStack().reversed()) {
                executionStackListAsString.add(s.toString());
            }
        executionStackList.setItems(FXCollections.observableList(executionStackListAsString));
        executionStackList.refresh();
    }
}