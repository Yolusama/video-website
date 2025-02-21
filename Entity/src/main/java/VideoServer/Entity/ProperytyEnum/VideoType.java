package VideoServer.Entity.ProperytyEnum;

public class VideoType extends CommonPropertyEnum{
    protected VideoType(Integer value) {
        super(value);
    }

    public static final VideoType Common = new VideoType(1);
    public static final VideoType Film = new VideoType(2);
    public static final VideoType SoapOpera = new VideoType(3);
    public static final VideoType ShortVideo = new VideoType(4);
    public static final VideoType Recording = new VideoType(5);
}
