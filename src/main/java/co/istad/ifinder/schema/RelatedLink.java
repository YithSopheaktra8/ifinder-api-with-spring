package co.istad.ifinder.schema;


import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RelatedLink {
    private List<String> websites;

    private List<String> socialMedia;

    private List<String> emails;

    private List<String> phoneNumbers;
}
