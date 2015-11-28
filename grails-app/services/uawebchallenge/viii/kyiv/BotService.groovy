package uawebchallenge.viii.kyiv

import grails.converters.JSON
import grails.transaction.Transactional
import grails.util.Holders

@Transactional
class BotService {


   //Methods for external calls
    def getExchangeRate() {
        Holders.config.externalApi.apicurrencyUsdToEur
        try {
            def textResponse = ['bash', '-c', "curl ${Holders.config.externalApi.callCurrencyUsdToEur}"].execute().text
            def jsonResponse = JSON.parse(textResponse)
            return jsonResponse
        }catch(Exception e){
            return false
        }



    }


    def getBashRandomQuote() {
        try {
            def source = new URL(Holders.config.externalApi.callBashRandomQuote).getText("WINDOWS-1251")
            source = source.find(/(<div class="text">)(.*)(div>)/)
            return source.substring(18, source.length()-6)
        }catch(Exception e){
            return false
        }

    }



    def getWeatherForecast(data) {
        def city = data.city.replaceAll(" ","_")
        def requestUrl = Holders.config.externalApi.baseWheatherApi + "?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        try {
            def url = requestUrl.toURL()
            def textResponse = url.text
            def jsonResponse = JSON.parse(textResponse)
            return jsonResponse
        }catch(Exception e){
            return false
        }
    }


    //Method for working with Telegram Api

    def sendMessage(data) {
        println data.chatId
        if (data.chatId == 152462118)
             return false
        try {
            def isMsg = Messages.findByMessage_idAndChat_id(data.messageId, data.chatId)
            if(isMsg == null){
                new Messages(message_id: data.messageId, chat_id: data.chatId, requestText: data.messageText).save(flush: true)
                ['bash', '-c', "curl -H \"Content-Type: application/json\" -X POST -d '{\"chat_id\":${data.chatId},\"text\":\"${data.messageText}\"}' https://api.telegram.org/bot146346142:AAHlXSKNwQ6jp7rXgh0krfX5z3ycrJjpTf4/sendMessage"].execute().text
            }
            return true
        }catch(Exception e){
            return false
        }
    }

    def getUpdates(){
        try {
            def textResponse = ['bash', '-c', "curl https://api.telegram.org/bot146346142:AAHlXSKNwQ6jp7rXgh0krfX5z3ycrJjpTf4/getUpdates"].execute().text
            def jsonResponse = JSON.parse(textResponse)
            return jsonResponse
        }catch(Exception e){
            return false
        }
    }

}
