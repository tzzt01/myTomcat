import java.util.stream.Stream;

public enum HttpStatusEnum {
    OK(200,"OK"), NOT_FOUND(404,"File Not Found");

    private Integer status;
    private String desc;

    HttpStatusEnum(Integer status,String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static HttpStatusEnum valueOf(Integer value){
        return Stream.of(values()).filter(status->status.getStatus().equals(value)).findAny().orElse(null);
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
