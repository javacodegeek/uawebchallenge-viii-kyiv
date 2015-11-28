package uawebchallenge.viii.kyiv

class Messages {

    String message_id
    String chat_id
    String requestText


    static constraints = {
        message_id(nullable: false, blank: false)
        chat_id(nullable: false, blank: false)
        requestText(nullable: true, blank: true, unique: true)


    }

    static mapping = {
        version false
        message_id column: 'message_id', index: 'User_name_Idx'
        chat_id column: 'chat_id'
        requestText column: 'requestText', sqlType: "Text"
    }
}
