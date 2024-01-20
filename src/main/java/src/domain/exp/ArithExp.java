package src.domain.exp;

import src.domain.exception.DivisionByZeroException;
import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.type.IntType;
import src.domain.type.Type;
import src.domain.value.IntValue;
import src.domain.value.Value;
public class ArithExp implements Exp{
    private final Exp e1;
    private final Exp e2;

    int op; //1-plus, 2-minus, 3-star, 4-divide

    public ArithExp(char c, Exp e1, Exp e2){
        this.e1 = e1;
        this.e2 = e2;
        if(c=='+') op=1;
        if(c=='-') op=2;
        if(c=='*') op=3;
        if(c=='/') op=4;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException{
        Value v1,v2;
        v1= e1.eval(symTbl, heap);
        if(v1.getType().equals(new IntType())){
            v2 = e2.eval(symTbl, heap);
            if(v2.getType().equals(new IntType())){
                IntValue i1 = (IntValue)v1;
                IntValue i2 = (IntValue)v2;
                int n1,n2;
                n1 = (int) i1.getVal();
                n2 = (int) i2.getVal();
                if(op==1) return new IntValue(n1+n2);
                if(op==2) return new IntValue(n1-n2);
                if(op==3) return new IntValue(n1*n2);
                if(op==4)
                    if(n2==0) throw new DivisionByZeroException();
                    else return new IntValue(n1/n2);
            }
            else throw new MyException("second operand is not an integer");
        }
        else throw new MyException("first operand is not an integer");
        return null;
    }

    @Override
    public String toString(){
        String s = "";
        if(op==1) s = e1.toString() + "+" + e2.toString();
        if(op==2) s = e1.toString() + "-" + e2.toString();
        if(op==3) s = e1.toString() + "*" + e2.toString();
        if(op==4) s = e1.toString() + "/" + e2.toString();
        return s;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1=e1.typeCheck(typeEnv);
        typ2=e2.typeCheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else
                throw new MyException("second operand is not an integer");
        }else
            throw new MyException("first operand is not an integer");
    }
}
