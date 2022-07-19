package com.rick.form

import monocle.Lens
import rx.{Ctx, Rx, Var}
import scalatags.JsDom
import shapeless._
import Framework._
import org.scalajs.dom.Event
import org.scalajs.dom.html.Input
import org.scalajs.dom.HTMLButtonElement

trait DomSyntax {
  def renderForm()(implicit owner: Ctx.Owner): JsDom.Modifier
}

object DomSyntax {
  implicit def domSyntax[T](a: Var[T])(implicit st: RenderDom[T]): DomSyntax = new DomSyntax {
    def renderForm()(implicit owner: Ctx.Owner): JsDom.Modifier = {
      st.renderForm(a, 0)
    }
  }

}
trait RenderDom[T] {
  def renderForm(t: Var[T], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier
}

object RenderDom extends LabelledTypeClassCompanion[RenderDom] {
  import scalatags.JsDom
  import scalatags.JsDom.all._
  def labeledInputDisabled(labelStr: Modifier, head: Modifier): JsDom.Modifier = {
    fieldset(
      cls := "col-md-4 mb-1",
      disabled := true,
      div(
        label(labelStr),
        head
      )
    )
  }
  def labeledInput(labelStr: Modifier, head: Modifier): JsDom.Modifier = {
    div(
      cls := "col-md-4 mb-1",
      label(labelStr),
      head
    )
  }

  def labeledCardGroup(labelStr: Modifier, head: Modifier): JsDom.Modifier = {
    div(cls := "col-md-12 mb-2", div(cls := "card", div(cls := "card-header", labelStr), div(cls := "card-body", head)))
  }

  def cardGroup(head: Modifier): JsDom.Modifier = {
    div(cls := "col-md-12 mb-2", div(cls := "card", div(cls := "card-body", head)))
  }

  def labeledCardFormRow(labelStr: Modifier, head: Modifier, btn: Modifier = div()): JsDom.Modifier = {
    labeledCardGroup(
      Seq(
        a(
          labelStr,
          cls := "btn text-secondary font-weight-bold float-left",
          style := "padding-left: 0"
        ),
        btn
      ),
      div(cls := "row", head)
    )
  }

  def inputDom(tpeValue: Modifier, t: Var[String])(implicit owner: Ctx.Owner): JsDom.Modifier = {
    val inputDom =
      input(
        tpeValue,
        cls := s"form-control",
        value := Rx {
          t()
        },
        onchange := Rx { e: Event =>
          e.target.valueOf() match {
            case inputEle: Input => {
              try {
                t.update(inputEle.value)
              } catch {
                case e: Throwable => {
                  inputEle.classList.add("is-invalid")
                }
              }
            }
          }
        }
      ).render
    inputDom
  }

  def btn(mod: Modifier, btnType: String = "primary")(implicit owner: Ctx.Owner): JsDom.Modifier = {
    button(
      tpe := "button",
      cls := s"btn btn-${btnType} float-end  ms-1",
      onmouseover := Rx { e: Event =>
        e.target match {
          case btn: HTMLButtonElement => {
            btn.focus()
          }
        }
      },
      mod
    )
  }

  implicit def stringDom: RenderDom[String] = new RenderDom[String] {
    def renderForm(t: Var[String], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "text", t)
    }
  }

  implicit def bigDecimalDom: RenderDom[BigDecimal] = new RenderDom[BigDecimal] {
    import scalatags.JsDom
    override def renderForm(t: Var[BigDecimal], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "number", t.zoom(Lens[BigDecimal, String](g => g.toString())(r => o => BigDecimal(r))))
    }
  }

  implicit def intDom: RenderDom[Int] = new RenderDom[Int] {
    override def renderForm(t: Var[Int], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "number", t.zoom(Lens[Int, String](g => g.toString)(r => o => r.toInt)))
    }
  }

  implicit def floatDom: RenderDom[Float] = new RenderDom[Float] {
    override def renderForm(t: Var[Float], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "number", t.zoom(Lens[Float, String](g => g.toString)(r => o => r.toFloat)))
    }
  }

  implicit def doubleDom: RenderDom[Double] = new RenderDom[Double] {
    override def renderForm(t: Var[Double], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "number", t.zoom(Lens[Double, String](g => g.toString)(r => o => r.toDouble)))
    }
  }

  implicit def longDom: RenderDom[Long] = new RenderDom[Long] {
    override def renderForm(t: Var[Long], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "number", t.zoom(Lens[Long, String](g => g.toString)(r => o => r.toLong)))
    }
  }

  implicit def booleanDom: RenderDom[Boolean] = new RenderDom[Boolean] {
    override def renderForm(t: Var[Boolean], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      inputDom(cls := "checkbox", t.zoom(Lens[Boolean, String](g => g.toString)(r => o => r.toBoolean)))
    }
  }

  implicit def seqDom[T](implicit st: RenderDom[T]): RenderDom[Seq[T]] = new RenderDom[Seq[T]] {

    override def renderForm(t: Var[Seq[T]], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      div(
        cls := "col-md-12",
        Rx {
          div(t().indices.map({ e =>
            val lens: Lens[Seq[T], T] =
              Lens({ m: Seq[T] => m(e) })(set => origin => origin.updated(e, set))
            st.renderForm(t.zoom(lens), level + 1)
          }))
        }
      )
    }
  }
  implicit def listDom[T](implicit st: RenderDom[T]): RenderDom[List[T]] = new RenderDom[List[T]] {

    override def renderForm(t: Var[List[T]], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier =
      div(
        cls := "col-md-12",
        Rx {
          div(t().indices.map({ e =>
            val lens: Lens[List[T], T] =
              Lens({ m: List[T] => m(e) })(set => origin => origin.updated(e, set))
            cardGroup(st.renderForm(t.zoom(lens), level + 1))
          }))
        }
      )
  }
  implicit def mapDom[T](implicit st: RenderDom[T]): RenderDom[Map[String, T]] = new RenderDom[Map[String, T]] {

    override def renderForm(t: Var[Map[String, T]], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
      Rx {
        div(
          t()
            .map({ e =>
              val l: Lens[Map[String, T], T] =
                Lens({ m: Map[String, T] => m(e._1) })(set => origin => origin + (e._1 -> set))

              labeledCardFormRow(s"${e._1}", st.renderForm(t.zoom(l), level + 1))
            })
            .toSeq
        )
      }
    }
  }

  object typeClass extends LabelledTypeClass[RenderDom] {
    def emptyProduct = new RenderDom[HNil] {

      override def renderForm(t: Var[HNil], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier =
        ""

    }

    def product[F, T <: HList](name: String, sh: RenderDom[F], st: RenderDom[T]) = new RenderDom[F :: T] {
      override def renderForm(ft: Var[F :: T], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
        if (name.equals("data")) {
          val headLens = Lens[F :: T, F](e => e.head)(e => e2 => e :: e2.tail)
          val tailLens = Lens[F :: T, T](e => e.tail)(e => e2 => e2.head :: e)
          val head = sh.renderForm(ft.zoom(headLens), level + 1)
          val tail = st.renderForm(ft.zoom(tailLens), level)
          Seq(
            head,
            tail
          )
        } else if (name.equals("head")) {
          val headLens = Lens[F :: T, F](e => e.head)(e => e2 => e :: e2.tail)
          val tailLens = Lens[F :: T, T](e => e.tail)(e => e2 => e2.head :: e)
          val head = sh.renderForm(ft.zoom(headLens), level + 1)
          val tail = st.renderForm(ft.zoom(tailLens), level)
          Seq(
            cardGroup(head),
            tail
          )
        } else if (name.equals("next$access$1")) {
          val headLens = Lens[F :: T, F](e => e.head)(e => e2 => e :: e2.tail)
          val tailLens = Lens[F :: T, T](e => e.tail)(e => e2 => e2.head :: e)
          val head = sh.renderForm(ft.zoom(headLens), level + 1)
          val tail = st.renderForm(ft.zoom(tailLens), level)
          Seq(
            cls := "mt-1",
            head,
            tail
          )
        } else {
          val headLens = Lens[F :: T, F](e => e.head)(e => e2 => e :: e2.tail)
          val tailLens = Lens[F :: T, T](e => e.tail)(e => e2 => e2.head :: e)
          val head = sh.renderForm(ft.zoom(headLens), level + 1)
          val tail = st.renderForm(ft.zoom(tailLens), level)
          Seq(
            labeledInput(name, head),
            tail
          )
        }

      }

    }

    def emptyCoproduct = new RenderDom[CNil] {
      override def renderForm(t: Var[CNil], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier =
        ""

    }

    def coproduct[L, R <: Coproduct](name: String, sl: => RenderDom[L], sr: => RenderDom[R]) = new RenderDom[L :+: R] {

      override def renderForm(lr: Var[L :+: R], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier =
        Rx {
          div(lr() match {
            case Inl(l) =>
              val lens = Lens[L :+: R, L] {
                case Inl(head) => head
                case Inr(tail) => throw new IllegalArgumentException("")
              }(set => origin => Inl(set))
              sl.renderForm(lr.zoom(lens), level)
            case Inr(r) =>
              val lens = Lens[L :+: R, R] {
                case Inl(head) => throw new IllegalArgumentException("")
                case Inr(tail) => tail
              }(set => origin => Inr(set))
              sr.renderForm(lr.zoom(lens), level)
          })
        }
    }

    def project[F, G](instance: => RenderDom[G], to: F => G, from: G => F) = new RenderDom[F] {
      override def renderForm(f: Var[F], level: Int)(implicit owner: Ctx.Owner): JsDom.Modifier = {
        val lens = Lens[F, G](to)(set => origin => from(set))
        instance.renderForm(f.zoom(lens), level)
      }
    }
  }
}
