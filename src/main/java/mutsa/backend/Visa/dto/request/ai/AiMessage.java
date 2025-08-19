package mutsa.backend.Visa.dto.request.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiMessage {
    private String role;
    private String content;  // 메시지 본문
}