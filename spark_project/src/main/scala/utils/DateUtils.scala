package utils

import java.util.Date
import org.apache.commons.lang3.time.FastDateFormat
object DateUtils {
  val OLD_FORMAT=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  val TARGET_FORMAT=FastDateFormat.getInstance("yyyyMMddHHmmss")

  def getTime(time:String) ={
    OLD_FORMAT.parse(time).getTime()
  }

  def parseTime(time:String) = {
    TARGET_FORMAT.format(new Date(getTime(time)))
  }
}
