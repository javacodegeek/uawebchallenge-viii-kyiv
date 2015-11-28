import grails.util.Holders
import uawebchallenge.viii.kyiv.BotService
import uawebchallenge.viii.kyiv.Messages

class MegamozkbotJob {

    BotService botService


    static triggers = {
        cron name: 'BotCronTrigger', startDelay: 1000, cronExpression: '*/10 * * ? * *'
    }

    def execute() {
        println "Bot cheking!"


        def upd = botService.getUpdates()



        upd.result.each { message ->

            def messageId = message.message.message_id
            def chatId = message.message.chat.id
            def messageText = message.message.text
            def fromFirstName = message.message.from.first_name


            def usrMsg = messageText.toLowerCase()
            def city

            if (usrMsg.contains("weather".toLowerCase())){
                def t = usrMsg.replaceAll("  "," ")
                t = t.split(" ")
                city = t[1]
                usrMsg = "weather"
            }

                switch (usrMsg) {
                    case "/start":
                        def messageTextS = "Yo $fromFirstName, I am Megamozk bot lets play with me! You can use me as you want!"
                        botService.sendMessage([messageId: messageId, chatId: chatId, messageText: messageTextS])
                        break
                    case "/help":
                        def helpText = "1." + Holders.config.telegramBot.megaMozkCommands.start.text + "  -  " + Holders.config.telegramBot.megaMozkCommands.start.description + "\n"
                        helpText += "2." + Holders.config.telegramBot.megaMozkCommands.help.text + "  -  " + Holders.config.telegramBot.megaMozkCommands.help.description + "\n"
                        helpText += "3." + Holders.config.telegramBot.megaMozkCommands.weatherForecast.text + "  -  " + Holders.config.telegramBot.megaMozkCommands.weatherForecast.description + "\n"
                        helpText += "4." + Holders.config.telegramBot.megaMozkCommands.exchangeRate.text + "  -  " + Holders.config.telegramBot.megaMozkCommands.exchangeRate.description + "\n"
                        helpText += "5." + Holders.config.telegramBot.megaMozkCommands.bashIm.text + "  -  " + Holders.config.telegramBot.megaMozkCommands.bashIm.description + "\n"
                        ['bash', '-c', "curl -H \"Content-Type: application/json\" -X POST -d '{\"chat_id\":\"$chatId\",\"text\":\"$helpText\"}' https://api.telegram.org/bot146346142:AAHlXSKNwQ6jp7rXgh0krfX5z3ycrJjpTf4/sendMessage"].execute().text
                        botService.sendMessage([messageId: messageId, chatId: chatId, messageText: helpText])
                        break
                    case "exchangerate":
                        def jsonResponseER = botService.getExchangeRate()
                        def msgTextER = "Result: rate = " +  jsonResponseER.rate + ", amount = " + jsonResponseER.amount + "\n"
                        msgTextER += "Information from https://currency-api.appspot.com/\n"
                        botService.sendMessage([messageId: messageId, chatId: chatId, messageText: msgTextER])
                        break
                    case "bash":
                        def textBash = botService.getBashRandomQuote()
                        botService.sendMessage([messageId: messageId, chatId: chatId, messageText: textBash])
                        break
                    case "weather":
                        def jsonResponsew = botService.getWeatherForecast([city: "Kherson"])
                        def results = jsonResponsew.query.results.channel
                        def cityw = results.location.city
                        def country = results.location.country

                        def atmosphereHumidity = results.atmosphere.humidity
                        def atmospherePressure = results.atmosphere.pressure
                        def atmosphereRising = results.atmosphere.rising
                        def atmosphereVisibility = results.atmosphere.visibility

                        def astronomySunrise = results.astronomy.sunrise
                        def astronomySunset = results.astronomy.sunset

                        def msgText = "Yor weather data\n"
                        msgText += "city: $cityw\n"
                        msgText += "country: $country\n"
                        msgText += "atmosphereHumidity: $atmosphereHumidity\n"
                        msgText += "atmospherePressure: $atmospherePressure\n"
                        msgText += "atmosphereRising: $atmosphereRising\n"
                        msgText += "atmosphereVisibility: $atmosphereVisibility\n"
                        msgText += "astronomySunrise: $astronomySunrise\n"
                        msgText += "astronomySunset: $astronomySunset\n"

                        msgText += "Information from https://developer.yahoo.com/weather/\n"

                        botService.sendMessage([messageId: messageId, chatId: chatId, messageText: msgText])
                        break
                    default:
                        break
                }

        }

    }
}
