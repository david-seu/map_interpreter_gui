package com.example.map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import src.controller.Controller;
import src.domain.exception.MyException;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.stmt.IfStmt;
import src.repo.IRepository;
import src.repo.InMemoryRepository;
import src.utils.Utils;


public class ListController {
    private MainController mainController;

    public void setProgramController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private ListView<IStmt> statements;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() {
        statements.setItems(FXCollections.observableArrayList(Utils.exampleList()));
        displayButton.setOnAction(actionEvent -> {
            int index = statements.getSelectionModel().getSelectedIndex();
            if (index < 0)
                return;
            PrgState prg = Utils.createPrgState(Utils.exampleList().get(index));
            IRepository repo = new InMemoryRepository(prg, "log" + (Utils.exampleList() .indexOf(prg)+1) + ".txt");
            Controller ctrl = new Controller(repo);
            try{
                MyException exception = Utils.typeChecker(Utils.exampleList().get(index), index+1);
                if(exception == null){
                    mainController.setController(ctrl);
                }
                else {
                    throw exception;
                }
            } catch (MyException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
            }
        });
    }

}
