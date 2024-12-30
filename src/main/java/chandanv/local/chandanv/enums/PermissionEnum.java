package chandanv.local.chandanv.enums;

public enum PermissionEnum {
    
    USER_CATALOGUE("user_catalogue"),
    PERMISSION("permission"),
    USER("user");

    private final String prefix;

    PermissionEnum(String prefix){
        this.prefix = prefix;
    }

    public String getPrefix(){
        return prefix;
    }


}
