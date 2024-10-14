package edu.npic.sps.features.file;

import edu.npic.sps.features.file.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileResponse uploadFile(MultipartFile file) throws IOException;
}
