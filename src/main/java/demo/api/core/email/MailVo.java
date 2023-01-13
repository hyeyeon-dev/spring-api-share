package demo.api.core.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailVo {
    private String fromAddress;
    private List<String> toAddressList = new ArrayList<>(); // 받는 사람
    private List<String> ccAddressList = new ArrayList<>(); // 참조
    private List<String> bccAddressList = new ArrayList<>();// 숨은 참조
    private String subject;
    private String content;
    private boolean isUseHtmlYn; // 메일 형식 HTML 여부
    private List<AttachFileVo> attachFileList = new ArrayList<>();
}
