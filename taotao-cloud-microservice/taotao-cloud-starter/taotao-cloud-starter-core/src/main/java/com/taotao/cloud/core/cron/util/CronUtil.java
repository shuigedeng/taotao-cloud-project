package com.taotao.cloud.core.cron.util;


import com.taotao.cloud.core.cron.pojo.CronField;
import com.taotao.cloud.core.cron.pojo.CronPosition;
import com.taotao.cloud.core.cron.pojo.DayOfYear;
import com.taotao.cloud.core.cron.pojo.TimeOfDay;
import java.util.*;

/**
 * 秒 分 时 日 月 周 (年)
 * "0 15 10 ? * *"  每天上午10:15触发
 * "0 0/5 14 * * ?"  在每天下午2点到下午2:55期间的每5分钟触发
 * "0 0-5 14 * * ?"  每天下午2点到下午2:05期间的每1分钟触发
 * "0 10,44 14 ? 3 WED"  三月的星期三的下午2:10和2:44触发
 * "0 15 10 ? * MON-FRI"  周一至周五的上午10:15触发
 * -------------------------------------------------------------------
 * 一、根据cron表达式，计算某天的那些时刻执行。
 * 思路：1、切割cron表达式
 *      2、转换每个域
 *      3、计算执行时间点（关键算法，解析cron表达式）
 *      4、计算某一天的哪些时间点执行
 * 二、根据cron表达式，给定Date，计算下一个执行时间点
 * 思路：1、找到所有时分秒的组合并按照时分秒排序
 *      2、给定的时分秒在以上集合之前、之后处理
 *      3、给定时时分秒在以上集合中找到一个最小的位置
 *      4、day+1循环直到找到满足月、星期的那一天
 * {@link https://www.cnblogs.com/junrong624/p/4239517.html}
 * {@link https://www.cnblogs.com/summary-2017/p/8974139.html}
 */
public class CronUtil {
    private static final int CRON_LEN      = 6;
    private static final int CRON_LEN_YEAR = 7;
    private static final String CRON_CUT   = "\\s+";

    private static final int MAX_ADD_YEAR  = 10;


    /**
     * 5.给定cron表达式和日期，计算满足cron的下一个执行时间点
     *
     * @param cron cron表达式
     * @param date 日期时间
     * @return 满足cron的下一个执行时间点
     */
    public static Date next(String cron, Date date) {
        List<CronField> cronFields = convertCronField(cron);

        CronField fieldSecond = cronFields.get(CronPosition.SECOND.ordinal());
        CronField fieldMinute = cronFields.get(CronPosition.MINUTE.ordinal());
        CronField fieldHour   = cronFields.get(CronPosition.HOUR.ordinal());

        CronField fieldDay    = cronFields.get(CronPosition.DAY.ordinal());
        CronField fieldMonth  = cronFields.get(CronPosition.MONTH.ordinal());
        CronField fieldWeek   = cronFields.get(CronPosition.WEEK.ordinal());

        Calendar calendar = Calendar.getInstance();
        //基准线,至少从下一秒开始
        calendar.setTime(date);
        calendar.add(Calendar.SECOND , 1);

        CronField fieldYear = null;
        /// 如果包含年域
        if (CRON_LEN_YEAR == cronFields.size()) {
            Integer year = DateUtil.year(calendar);
            fieldYear = cronFields.get(CronPosition.YEAR.ordinal());
            List<Integer> listYear = fieldYear.points();
            Integer calYear = CompareUtil.findNext(year, listYear);
            if (!year.equals(calYear)) {
                calendar.set(Calendar.YEAR, calYear);
            }
        }

        return doNext(calendar, fieldSecond, fieldMinute, fieldHour, fieldDay, fieldMonth, fieldWeek , fieldYear);
    }

    public static Date doNext(Calendar calendar, CronField fieldSecond, CronField fieldMinute, CronField fieldHour, CronField fieldDay, CronField fieldMonth, CronField fieldWeek , CronField fieldYear) {
        //////////////////////////////////时分秒///////////////////////////////
        TimeOfDay timeOfDayMin = doTimeOfDay(calendar, fieldSecond, fieldMinute, fieldHour);

        //////////////////////////////////日月周///////////////////////////////
        return doDayOfYear(0 , calendar, fieldDay, fieldMonth, fieldWeek , fieldYear , timeOfDayMin);
    }

    /**
     * 处理日月周
     */
    private static Date doDayOfYear(int addYear , Calendar calendar, CronField fieldDay, CronField fieldMonth, CronField fieldWeek , CronField fieldYear , TimeOfDay timeOfDayMin) {
        if(addYear >= MAX_ADD_YEAR){
            throw new IllegalArgumentException("Invalid cron expression【日月周年】 which led to runaway search for next trigger");
        }

        int year = DateUtil.year(calendar);
        //有年域的情况
        if(null != fieldYear){
            //这一年不满足加一年,时分秒日月都重置为最小的
            if(!satisfy(year , fieldYear)){
                addOneYear(calendar, timeOfDayMin);
                return doDayOfYear(++addYear , calendar, fieldDay, fieldMonth, fieldWeek, fieldYear, timeOfDayMin);
            }

        }
        //先确定日月
        int dayNow    = DateUtil.day(calendar);
        int monthNow  = DateUtil.month(calendar);


        //可用的日月
        List<Integer> listDay   = fieldDay.points();
        List<Integer> listMonth = fieldMonth.points();


        DayOfYear dayOfYearNow = new DayOfYear(dayNow , monthNow , year);
        //找到最小的一个满足日月星期的
        DayOfYear dayOfYearMin = findMinDayOfYear(dayOfYearNow, listDay, listMonth, fieldWeek);

        //这一年不满足加一年,时分秒日月都重置为最小的
        if(null == dayOfYearMin){
            addOneYear(calendar , timeOfDayMin);
            return doDayOfYear(++addYear , calendar, fieldDay, fieldMonth, fieldWeek, fieldYear, timeOfDayMin);
        }

        //最小的即是要找的day，因为前面一个方法已经处理好时分秒了
        setDayOfYear(calendar, dayOfYearMin);

        //小于最小的的时候
        if (dayOfYearNow.compareTo(dayOfYearMin) < 0) {
            setTimeOfDay(calendar , timeOfDayMin);
        }

        return calendar.getTime();
    }

    /**
     * 找到最小的满足日月周的
     */
    private static DayOfYear findMinDayOfYear(DayOfYear dayOfYearNow, List<Integer> listDay, List<Integer> listMonth, CronField fieldWeek) {
        for (Integer month : listMonth) {
            for (Integer day : listDay) {
                DayOfYear dayOfYear = new DayOfYear(day, month , dayOfYearNow.getYear());
                //大于等于现在的并且满足星期的
                if(dayOfYear.compareTo(dayOfYearNow) >= 0
                        && satisfy(dayOfYear.week() , fieldWeek)
                        ){
                    return dayOfYear;
                }
            }
        }
        return null;
    }

    /**
     * 加一年 时分秒日月都重置为最小的
     */
    private static void addOneYear(Calendar calendar, TimeOfDay timeOfDayMin) {
        setDayOfYear(calendar , 1 ,1);
        setTimeOfDay(calendar, timeOfDayMin);
        calendar.add(Calendar.YEAR, 1);
    }

    /**
     * 设置日月
     */
    private static void setDayOfYear(Calendar calendar, DayOfYear dayOfYear) {
        setDayOfYear(calendar , dayOfYear.getMonth() , dayOfYear.getDay());
    }
    private static void setDayOfYear(Calendar calendar, int month , int day) {
        calendar.set(Calendar.MONTH , month - 1);
        calendar.set(Calendar.DAY_OF_MONTH , day);
    }

    /**
     * 处理时分秒，并返回最小的时分秒
     */
    private static TimeOfDay doTimeOfDay(Calendar calendar, CronField fieldSecond, CronField fieldMinute, CronField fieldHour) {
        //先确定时分秒
        int hourNow    = DateUtil.hour(calendar);
        int minuteNow  = DateUtil.minute(calendar);
        int secondNow  = DateUtil.second(calendar);


        //找到所有时分秒的组合
        List<TimeOfDay> points = timesOfDay(fieldHour, fieldMinute, fieldSecond);

        TimeOfDay timeOfDayNow   = new TimeOfDay(hourNow, minuteNow, secondNow);
        TimeOfDay timeOfDayMin   = points.get(0);
        TimeOfDay timeOfDayMax   = points.get(points.size() - 1);
        //小于最小的
        if (timeOfDayNow.compareTo(timeOfDayMin) < 0) {
            setTimeOfDay(calendar, timeOfDayMin);
            //大于最大的
        } else if (timeOfDayNow.compareTo(timeOfDayMax) > 0) {
            setTimeOfDay(calendar, timeOfDayMin);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            TimeOfDay next = CompareUtil.findNext(timeOfDayNow, points);
            setTimeOfDay(calendar, next);
        }
        return timeOfDayMin;
    }

    /**
     * 设置时分秒域
     */
    private static void setTimeOfDay(Calendar calendar, TimeOfDay timeOfDay) {
        calendar.set(Calendar.HOUR_OF_DAY, timeOfDay.getHour());
        calendar.set(Calendar.MINUTE, timeOfDay.getMinute());
        calendar.set(Calendar.SECOND, timeOfDay.getSecond());
    }


    /**
     * 4.计算cron表达式在某一天的那些时间执行,精确到秒
     *
     * @param cron cron表达式
     * @param date 时间,某天
     * @return 这一天的哪些时分秒执行, 不执行的返回空
     */
    public static List<TimeOfDay> timesOfDay(String cron, Date date) {
        List<CronField> cronFields = convertCronField(cron);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year  = DateUtil.year(calendar);
        int week  = DateUtil.week(calendar);
        int month = DateUtil.month(calendar);
        int day   = DateUtil.day(calendar);
        /// 如果包含年域
        if (CRON_LEN_YEAR == cronFields.size()) {
            CronField fieldYear = cronFields.get(CronPosition.YEAR.ordinal());
            if (!satisfy(year, fieldYear)) {
                return Collections.emptyList();
            }
        }

        CronField fieldWeek  = cronFields.get(CronPosition.WEEK.ordinal());
        CronField fieldMonth = cronFields.get(CronPosition.MONTH.ordinal());
        CronField fieldDay   = cronFields.get(CronPosition.DAY.ordinal());
        ///今天不执行就直接返回空
        if (!satisfy(week, fieldWeek)
                || !satisfy(month, fieldMonth)
                || !satisfy(day, fieldDay)) {
            return Collections.emptyList();
        }

        CronField fieldHour      = cronFields.get(CronPosition.HOUR.ordinal());
        CronField fieldMinute    = cronFields.get(CronPosition.MINUTE.ordinal());
        CronField fieldSecond    = cronFields.get(CronPosition.SECOND.ordinal());

        return timesOfDay(fieldHour, fieldMinute, fieldSecond);
    }

    public static List<TimeOfDay> timesOfDay(CronField fieldHour, CronField fieldMinute, CronField fieldSecond) {
        List<Integer> listHour   = fieldHour.points();
        List<Integer> listMinute = fieldMinute.points();
        List<Integer> listSecond = fieldSecond.points();

        List<TimeOfDay> points = new ArrayList<>(listHour.size() * listMinute.size() * listSecond.size());
        for (Integer hour : listHour) {
            for (Integer minute : listMinute) {
                for (Integer second : listSecond) {
                    points.add(new TimeOfDay(hour, minute, second));
                }
            }
        }
        return points;
    }

    /**
     * 给定一个值,看是否满足cron表达示
     * @param fieldValue 给定值
     * @param field 域
     * @return *或者值在集合中
     */
    public static boolean satisfy(int fieldValue, CronField field) {
        //利用 || 的短路特性可以避免 points 计算 , 并且 points本身是有缓存的
        return field.containsAll() || CompareUtil.inList(fieldValue, field.points());
    }

    /**
     * 2.cron域表达式转换为域
     */
    public static List<CronField> convertCronField(String cron) {
        List<String> cut = cut(cron);
        int size = cut.size();
        if (CRON_LEN != size && (CRON_LEN + 1) != size) {
            throw new IllegalArgumentException("cron表达式必须有六个域或者七个域(最后为年)");
        }
        List<CronField> cronFields = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            CronPosition cronPosition = CronPosition.fromPosition(i);
            cronFields.add(new CronField(
                    cronPosition,
                    CronShapingUtil.shaping(cut.get(i), cronPosition)
            ));
        }
        return cronFields;
    }

    /**
     * 1.把cron表达式切成域表达式
     *
     * @param cron cron
     * @return 代表每个域的列表
     */
    public static List<String> cut(String cron) {
        cron = cron.trim();
        String[] split = cron.split(CRON_CUT);
        return Arrays.asList(split);
    }
}
