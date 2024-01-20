package src.domain.exp;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.type.BoolType;
import src.domain.type.Type;
import src.domain.value.BoolValue;
import src.domain.value.Value;

public class LogicExp implements Exp{
    private final Exp e1;
    private final Exp e2;
    int op; // 1 for and, 2 for or

    public LogicExp(char c, Exp e1, Exp e2){
        this.e1 = e1;
        this.e2 = e2;
        if(c=='&') op=1;
        if(c=='|') op=2;
    }

    public Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException{
        Value v1,v2;
        v1 = e1.eval(symTbl, heap);
        if(v1.getType().equals(new BoolType())){
            v2 = e2.eval(symTbl, heap);
            if(v2.getType().equals(new BoolType())){
                BoolValue b1 = (BoolValue)v1;
                BoolValue b2 = (BoolValue)v2;
                boolean n1,n2;
                n1 = (boolean) b1.getVal();
                n2 = (boolean) b2.getVal();
                if(op==1) return new BoolValue(n1&&n2);
                if(op==2) return new BoolValue(n1||n2);
            }
            else throw new MyException("second operand is not a boolean");
        }
        else throw new MyException("first operand is not a boolean");
        return null;
    }

    public String toString(){
        String s = "";
        if(op==1) s = e1.toString() + "&&" + e2.toString();
        if(op==2) s = e1.toString() + "||" + e2.toString();
        return s;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type1, type2;
        type1 = e1.typeCheck(typeEnv);
        type2 = e2.typeCheck(typeEnv);
        if(type1.equals(new BoolType())){
            if(type2.equals(new BoolType())){
                return new BoolType();
            }
            else throw new MyException("second operand is not a boolean");
        }
        else throw new MyException("first operand is not a boolean");
    }
}
