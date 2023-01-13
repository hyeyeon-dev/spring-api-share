package demo.api.core.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileVo {
    private String realFileName;
    private String attachFileName;
}
