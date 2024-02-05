package com.example.ddmdemo.service.impl;

import com.example.ddmdemo.exceptionhandling.exception.LoadingException;
import com.example.ddmdemo.exceptionhandling.exception.StorageException;
import com.example.ddmdemo.indexmodel.DocumentIndex;
import com.example.ddmdemo.indexrepository.DocumentIndexRepository;
import com.example.ddmdemo.model.DummyTable;
import com.example.ddmdemo.respository.ElasticSearchRepository;
import com.example.ddmdemo.service.interfaces.FileService;
import com.example.ddmdemo.service.interfaces.IndexingService;
import com.example.ddmdemo.service.interfaces.LocationService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private final DocumentIndexRepository documentIndexRepository;

    private final ElasticSearchRepository documentRepository;

    private final FileService fileService;

    private final LanguageDetector languageDetector;

    private final LocationService locationService;


    @Override
    @Transactional
    public String indexDocument(MultipartFile documentFile) {
        var newEntity = new DummyTable();
        var newIndex = new DocumentIndex();

        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newIndex.setTitle(title);
        newEntity.setTitle(title);

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newIndex.setContentSr(documentContent);
        } else {
            newIndex.setContentEn(documentContent);
        }
        newEntity.setTitle(title);

        parsingText(documentContent, newIndex);

        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());
        newIndex.setServerFilename(serverFilename);
        newEntity.setServerFilename(serverFilename);

        newEntity.setMimeType(detectMimeType(documentFile));
        var savedEntity = documentRepository.save(newEntity);

        newIndex.setDatabaseId(savedEntity.getId());

        documentIndexRepository.save(newIndex);

        return serverFilename;
    }

    private void parsingText(String documentContent, DocumentIndex newIndex) {
        Pattern pattern = Pattern.compile("Uprava za (.*?),");
        Matcher matcher = pattern.matcher(documentContent);
        if(matcher.find()) newIndex.setGovernmentName(matcher.group(1));

        pattern = Pattern.compile("nivo uprave: (.*?),");
        matcher = pattern.matcher(documentContent);
        if(matcher.find()) newIndex.setGovernmentType(matcher.group(1));

        pattern = Pattern.compile("nivo uprave: (.*?), (.*?,\\d+,.+?) u");
        matcher = pattern.matcher(documentContent);
        if(matcher.find()) {
            newIndex.setGovernmentAddress(matcher.group(2));
            var coordinates = locationService.getCoordinates(matcher.group(2));
            newIndex.setLocation(new GeoPoint(coordinates[1], coordinates[0]));
        }


        pattern = Pattern.compile("\\n(.*?)\\r\\nPotpisnik ugovora za klijenta");
        matcher = pattern.matcher(documentContent);
        if(matcher.find()) {
            newIndex.setName(matcher.group(1).split(" ")[0]);
            newIndex.setSurname(matcher.group(1).split(" ")[1]);
        }
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new LoadingException("Error while trying to load PDF file content.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }

    private String detectMimeType(MultipartFile file) {
        var contentAnalyzer = new Tika();

        String trueMimeType;
        String specifiedMimeType;
        try {
            trueMimeType = contentAnalyzer.detect(file.getBytes());
            specifiedMimeType =
                Files.probeContentType(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new StorageException("Failed to detect mime type for file.");
        }

        if (!trueMimeType.equals(specifiedMimeType) &&
            !(trueMimeType.contains("zip") && specifiedMimeType.contains("zip"))) {
            throw new StorageException("True mime type is different from specified one, aborting.");
        }

        return trueMimeType;
    }
}