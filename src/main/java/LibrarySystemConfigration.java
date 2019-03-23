import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class LibrarySystemConfigration extends Configuration {
    @NotEmpty
    private String libraryName;

    @JsonProperty
    public void setLibraryName(String libraryName){
        this.libraryName=libraryName;
    }
    
    @JsonProperty
    public String getLibraryName(){
        return libraryName;
    }
}
