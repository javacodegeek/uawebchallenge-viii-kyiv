class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "/default"(view:"/default")
        "500"(view:'/error')
        "/test"(controller: "bot", action: "test")
        "/bot/commands"(controller: "bot", action: "getAvailableCommands")
        "/bot/updates"(controller: "bot", action: "getUpdates")
	}
}
