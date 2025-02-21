package VideoServer.Entity.ProperytyEnum;

public abstract class CommonPropertyEnum {
    private final Integer value;
    protected CommonPropertyEnum(Integer value){
        this.value = value;
    }

    public Integer Value(){
        return value;
    }
}
