package src.domain.commands;

import src.controller.Controller;
import src.domain.exception.MyException;

import java.io.IOException;

public class RunExample extends Command{
    private final Controller controller;

    public RunExample(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try{
            controller.allStep();
        }catch (MyException | InterruptedException | IOException e){
            System.out.println(e.getMessage());
        }
    }

}
