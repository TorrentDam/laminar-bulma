package default

import com.github.lavrov.bittorrent.app.protocol.{Command, Event}
import com.raquo.airstream.core.{EventStream, Observer}
import com.raquo.laminar.api.L._


object SearchPage {

  def apply(send: Observer[Command.Search], receive: EventStream[Event.SearchResults]) = {

    val searchTermVar = Var(initial = "")

    div(
      cls := "container is-max-desktop",
      form(cls := "block",
        onSubmit.preventDefault.mapTo(Command.Search(searchTermVar.now)) --> send,
        div(cls := "field has-addons",
          div(cls := "control is-expanded",
            input(
              cls := "input is-primary is-large",
              typ := "text",
              placeholder := "Search",
              controlled(
                value <-- searchTermVar,
                onInput.mapToValue --> searchTermVar
              )
            )
          ),
          div(cls := "control is-hidden-mobile",
            button(cls := "button is-primary is-large", "Go")
          )
        )
      ),
      children <-- receive.map { results =>
        results.entries.map { entry =>
          div(cls := "media",
            div(cls := "media-content",
              div(cls := "content",
                p(
                  a(
                    strong(entry.name),
                    onClick --> {_ => Routing.router.pushState(Routing.Page.Torrent(entry.infoHash))},
                  ),
                  br(),
                  span(cls := "tags mb-0",
                    entry.ext.map( ext =>
                      span(cls := "tag is-light", ext)
                    )
                  )
                )
              )
            )
          )
        }
      }
    )
  }
}
