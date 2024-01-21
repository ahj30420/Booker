package project.booker.controller.MessageController.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriteMessage {

    private String recipientId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String content;

}
