package VideoServer.Entity.ProperytyEnum;

public class UserType extends CommonPropertyEnum{
    protected UserType(Integer value) {
        super(value);
    }

    public static final UserType Guest = new UserType(1);
    public static final UserType Common = new UserType(2);
    public static final UserType VIP = new UserType(3);
    public static final UserType Admin = new UserType(4);
}
