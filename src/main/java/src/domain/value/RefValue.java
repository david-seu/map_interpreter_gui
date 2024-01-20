package src.domain.value;

import src.domain.type.RefType;
import src.domain.type.Type;

public class RefValue implements Value{
    Integer address;
    Type locationType;


    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    public Type getType(){
        return new RefType(locationType);
    }
//
//    public Type getLocationType(){
//        while(locationType instanceof RefType){
//            locationType = ((RefType) locationType).getInner();
//        }
//        return locationType;
//    }

    public Object getVal(){
        return address;
    }

    public String toString(){
        return "(" + Integer.toString(address) + ", " + locationType.toString() + ")";
    }

    public boolean equals(Object another){
        if(another instanceof RefValue){
            return address == ((RefValue) another).getVal() && (new RefType(locationType)).equals(((RefValue) another).getType());
        }
        return false;
    }

    @Override
    public Value deepCopy() {
        return new RefValue(address, locationType);
    }

}
