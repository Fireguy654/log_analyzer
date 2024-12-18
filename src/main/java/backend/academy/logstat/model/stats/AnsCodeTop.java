package backend.academy.logstat.model.stats;

import java.util.Map;

public class AnsCodeTop extends TopValueLogStat {
    private static final Map<Long, String> DESCRIPTIONS = Map.ofEntries(
            Map.entry(100L, "Continue"),
            Map.entry(101L, "Switching Protocols"),
            Map.entry(102L, "Processing"),
            Map.entry(103L, "Early Hints"),
            Map.entry(200L, "OK"),
            Map.entry(201L, "Created"),
            Map.entry(202L, "Accepted"),
            Map.entry(203L, "Non-Authoritative Information"),
            Map.entry(204L, "No Content"),
            Map.entry(205L, "Reset Content"),
            Map.entry(206L, "Partial Content"),
            Map.entry(207L, "Multi-Status"),
            Map.entry(208L, "Already Reported"),
            Map.entry(218L, "This is fine (Apache Web Server)"),
            Map.entry(226L, "IM Used"),
            Map.entry(300L, "Multiple Choices"),
            Map.entry(301L, "Moved Permanently"),
            Map.entry(302L, "Found"),
            Map.entry(303L, "See Other"),
            Map.entry(304L, "Not Modified"),
            Map.entry(306L, "Switch Proxy"),
            Map.entry(307L, "Temporary Redirect"),
            Map.entry(308L, "Resume Incomplete"),
            Map.entry(400L, "Bad Request"),
            Map.entry(401L, "Unauthorized"),
            Map.entry(402L, "Payment Required"),
            Map.entry(403L, "Forbidden"),
            Map.entry(404L, "Not Found"),
            Map.entry(405L, "Method Not Allowed"),
            Map.entry(406L, "Not Acceptable"),
            Map.entry(407L, "Proxy Authentication Required"),
            Map.entry(408L, "Request Timeout"),
            Map.entry(409L, "Conflict"),
            Map.entry(410L, "Gone"),
            Map.entry(411L, "Length Required"),
            Map.entry(412L, "Precondition Failed"),
            Map.entry(413L, "Request Entity Too Large"),
            Map.entry(414L, "Request-URI Too Long"),
            Map.entry(415L, "Unsupported Media Type"),
            Map.entry(416L, "Requested Range Not Satisfiable"),
            Map.entry(417L, "Expectation Failed"),
            Map.entry(418L, "I'm a teapot"),
            Map.entry(419L, "Page Expired (Laravel Framework)"),
            Map.entry(420L, "Method Failure (Spring Framework)"),
            Map.entry(421L, "Misdirected Request"),
            Map.entry(422L, "Unprocessable Entity"),
            Map.entry(423L, "Locked"),
            Map.entry(424L, "Failed Dependency"),
            Map.entry(426L, "Upgrade Required"),
            Map.entry(428L, "Precondition Required"),
            Map.entry(429L, "Too Many Requests"),
            Map.entry(431L, "Request Header Fields Too Large"),
            Map.entry(440L, "Login Time-out"),
            Map.entry(444L, "Connection Closed Without Response"),
            Map.entry(449L, "Retry With"),
            Map.entry(450L, "Blocked by Windows Parental Controls"),
            Map.entry(451L, "Unavailable For Legal Reasons"),
            Map.entry(494L, "Request Header Too Large"),
            Map.entry(495L, "SSL Certificate Error"),
            Map.entry(496L, "SSL Certificate Required"),
            Map.entry(497L, "HTTP Request Sent to HTTPS Port"),
            Map.entry(498L, "Invalid Token (Esri)"),
            Map.entry(499L, "Client Closed Request"),
            Map.entry(500L, "Internal Server Error"),
            Map.entry(501L, "Not Implemented"),
            Map.entry(502L, "Bad Gateway"),
            Map.entry(503L, "Service Unavailable"),
            Map.entry(504L, "Gateway Timeout"),
            Map.entry(505L, "HTTP Version Not Supported"),
            Map.entry(506L, "Variant Also Negotiates"),
            Map.entry(507L, "Insufficient Storage"),
            Map.entry(508L, "Loop Detected"),
            Map.entry(509L, "Bandwidth Limit Exceeded"),
            Map.entry(510L, "Not Extended"),
            Map.entry(511L, "Network Authentication Required"),
            Map.entry(520L, "Unknown Error"),
            Map.entry(521L, "Web Server Is Down"),
            Map.entry(522L, "Connection Timed Out"),
            Map.entry(523L, "Origin Is Unreachable"),
            Map.entry(524L, "A Timeout Occurred"),
            Map.entry(525L, "SSL Handshake Failed"),
            Map.entry(526L, "Invalid SSL Certificate"),
            Map.entry(527L, "Railgun Listener to Origin Error"),
            Map.entry(530L, "Origin DNS Error"),
            Map.entry(598L, "Network Read Timeout Error")
    );

    private static final long AMOUNT = 5;

    @Override
    public String header() {
        return "Most usual codes of answers";
    }

    @Override
    public void set(BasicReport basicReport) {
        values = getTopN(
            basicReport.ansCodes(),
            code -> code + "(" + DESCRIPTIONS.getOrDefault(code, "Status not supported") + ")",
            AMOUNT
        );
    }
}
