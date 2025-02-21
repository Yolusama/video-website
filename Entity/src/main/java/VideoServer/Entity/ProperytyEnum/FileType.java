package VideoServer.Entity.ProperytyEnum;

public class FileType extends CommonPropertyEnum{
    protected FileType(Integer value) {
        super(value);
    }

    public static final FileType UserAvatar = new FileType(1);
    public static final FileType VideoCover = new FileType(2);
    public static final FileType ShortVideo = new FileType(3);
    public static final FileType LongVideo = new FileType(4);
}
