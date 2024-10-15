package com.universityproject.backendproject.model.dto.application.request;

import com.universityproject.backendproject.model.dto.application.request.ApplicationDetailsRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApplicationCompositeRequest {
    private ApplicationRequest applicationRequest;
    private ApplicationDetailsRequest applicationDetailsRequest;
}
