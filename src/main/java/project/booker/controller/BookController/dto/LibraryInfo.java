package project.booker.controller.BookController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LibraryInfo {

    private String libName;
    private String address;
    private String tel;
    private String operatingTime;
    private String closed;
    private String latitude;
    private String longitude;

    @Builder
    public LibraryInfo(String libName, String address, String tel, String operatingTime, String closed, String latitude, String longitude) {
        this.libName = libName;
        this.address = address;
        this.tel = tel;
        this.operatingTime = operatingTime;
        this.closed = closed;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
