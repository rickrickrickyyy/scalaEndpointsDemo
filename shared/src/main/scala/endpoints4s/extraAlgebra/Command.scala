package endpoints4s.extraAlgebra

sealed trait Command

case class Done() extends Command

case class HeartBeat() extends Command

case class MovingRemark(remark: String, timeStamp: Long) extends Command

case class StaticRemark(remark: String, timeStamp: Long) extends Command

object Command {
  val DONE: Done = Done()
  val HEART_BEAT: HeartBeat = HeartBeat()
}
