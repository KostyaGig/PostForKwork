package ru.kostya.postforkowrk.constans;

public class Firebase {

    public static final String USER_REF = "Users";

    //For login user
    public static final String ERROR_CREATE_ACCOUNT= "ErrorRegisterUser";
    public static final String SUCCESS_REGISTER_USER = "Success";

    //For sign in user
    public static final String SUCCESS_SIGN_IN_USER = "Success";
    public static final String ERROR_SIGN_IN_USER = "ErrorSignInUser";
    public static final String ERROR_ADD_USER = "ErrorAddUser";

    //for manactivity
    public static final String EXTRA_USER_EMAIL = "UserEmail";

    //for user
    public static final String NAME_USER = "NameUser";
    public static final String EMAIL_USER = "EmailUser";
    public static final String PASSWORD_USER = "PasswordUser";

    public static final String IMAGE_URL_USER = "ImageUrlUser";
    public static final String EXTENSION_IMAGE_URL_USER = "ExtenstionImageUrlUser";

    //Для проверки юзера в realtimedb
    public static final String USER_EXISTING = "UserExist";
    public static final String USER_NOT_EXISTING = "UserNotExisting";

    //for startactivityresult post,имя publisher'a and his name
    public static final String PUBLISHER_IMAGE_URL = "PublisherImageUrl";
    public static final String PUBLISHER_NAME = "PublisherName";

    //После добавления поста мы попадаем в onactivity result in mainactivty , эти константы нужны для получения названия поста ,его текста ,фотки и так далее

    public static final String TITLE_POST = "TitlePost";
    public static final String TEXT_POST = "TextPost";
    public static final String IMAGE_URL_POST = "ImageUrlPost";
    public static final String HOUR_PUBLISH_POST = "HourPublishPost";

    //Для получения publisher image url,фотки пользователя который публикует данный пост и его имени воспользуемся 2 - мя константами выше

}
