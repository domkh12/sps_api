package edu.npic.sps.features.file;

import edu.npic.sps.features.file.dto.FileResponse;
import edu.npic.sps.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService{

    @Value("${file-server.server-path}")
    private String serverPath;

    @Value("${file-server.base-uri}")
    private String baseUri;

    @Override
    public FileResponse uploadFile(MultipartFile file) throws IOException {

        String newFileName = FileUtil.generateFileName(file.getOriginalFilename());
        String extension = FileUtil.extractExtension(file.getOriginalFilename());

        Path path = Path.of(serverPath + newFileName);
        Files.copy(file.getInputStream(), path);
        return FileResponse.builder()
                .name(newFileName)
                .size(file.getSize())
                .extension(extension)
                .uri(baseUri + newFileName)
                .build();
    }
}
