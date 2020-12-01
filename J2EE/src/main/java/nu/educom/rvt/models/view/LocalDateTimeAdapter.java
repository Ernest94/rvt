package nu.educom.rvt.models.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
	
    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
        return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    @Override
    public String marshal(LocalDateTime dateTime) throws Exception {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime);
    }   
}