package fi.csc.avaa.kuhiti.logging;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import fi.csc.avaa.tools.Const;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for logging statistical data from custom portlets in AVAA portal
 * to Liferay Tomcat logs.
 *
 * @author jmlehtin
 */
public final class AvaaStatisticsLogger {

    private Log log;
    private static final String LOG_ROW_PREFIX = "#";
    private static final String SERVICE_AVAA = "AVAA";
    private static final String LOG_EVENT_DOWNLOAD = "DOWNLOAD";
    private static final String LOG_EVENT_ACCESS = "ACCESS";
    private static final String NOT_AVAILABLE = "NA";

    public AvaaStatisticsLogger(String className) {
        this.log = LogFactoryUtil.getLog(className);
    }

    public void logDownloadEvent(String application, String dataset, long sizeInBytes, LocalDateTime timestamp) {
        String logRow = LOG_ROW_PREFIX + LOG_EVENT_DOWNLOAD + Const.STRING_SPACE + SERVICE_AVAA + Const.STRING_SPACE + application + Const.STRING_SPACE + dataset + Const.STRING_SPACE + (sizeInBytes > 0 ? String.valueOf(sizeInBytes) : NOT_AVAILABLE) + Const.STRING_SPACE + timestamp.format(getDateTimeFormatter());
        log.info(logRow);
    }

    public void logDownloadEvent(String application, String dataset, LocalDateTime timestamp) {
        logDownloadEvent(application, dataset, 0L, timestamp);
    }

    public void logAppAccessEvent(String application, LocalDateTime timestamp) {
        String logRow = LOG_ROW_PREFIX + LOG_EVENT_ACCESS + Const.STRING_SPACE + SERVICE_AVAA + Const.STRING_SPACE + application + Const.STRING_SPACE + timestamp.format(getDateTimeFormatter());
        log.info(logRow);
    }

    private DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

}
