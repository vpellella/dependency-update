package com.dependency.update.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DependencyUpdateService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DependencyUpdateService.class);

	private static final String TABS_TO_ADD = "TABS_TO_ADD";

	@Value("${dependency.update.location}")
	private String location;

	@Value("${dependency.update.from.groupId}")
	private String dependencyToUpdateFromGroupId;

	@Value("${dependency.update.from.artifactId}")
	private String dependencyToUpdateFromArtifactId;

	@Value("${dependency.update.from.version:}")
	private String dependencyToUpdateFromVersion;

	@Value("${dependency.update.to.version}")
	private String dependencyToUpdateToVersion;

	public boolean updateDependency() throws IOException {
		LOG.debug("beginning of update in DependencyUpdateService");
		verifyInputValues();
		LOG.info("====================================================");
		LOG.info("Group ID:     " + dependencyToUpdateFromGroupId);
		LOG.info("Artifact ID:  " + dependencyToUpdateFromArtifactId);
		LOG.info("Old Version:  " + dependencyToUpdateFromVersion);
		LOG.info("New Version:  " + dependencyToUpdateToVersion);
		LOG.info("====================================================");

		String dependencyAfterUpdate = "<groupId>" + dependencyToUpdateFromGroupId + "</groupId>\n" + TABS_TO_ADD +
				"<artifactId>" + dependencyToUpdateFromArtifactId + "</artifactId>\n" + TABS_TO_ADD +
				"<version>" + dependencyToUpdateToVersion +"</version>";
				
		StringBuilder dependencyBeforeUpdate = new StringBuilder();
		dependencyBeforeUpdate.append("(<groupId>" +  dependencyToUpdateFromGroupId + "</groupId>)");
		dependencyBeforeUpdate.append("[ \n\t]*");
		dependencyBeforeUpdate.append("(<artifactId>" + dependencyToUpdateFromArtifactId +"</artifactId>)");
		dependencyBeforeUpdate.append("[ \n\t]*");
		if(dependencyToUpdateFromVersion == null || dependencyToUpdateFromVersion.isEmpty())
			dependencyBeforeUpdate.append("(<version>[0-9.]*</version>)");
		else
			dependencyBeforeUpdate.append("(<version>" + dependencyToUpdateFromVersion + "</version>)");
		
		Path locationPath = Paths.get(location);
		Files.list(locationPath).forEach(path -> {
			Path pomFilePath = path.resolve(path + "/pom.xml");
			if (Files.exists(pomFilePath)) {
				try {
					LOG.debug("pom file to be update at "+ path);
					update(pomFilePath, dependencyBeforeUpdate.toString(), dependencyAfterUpdate);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		return true;
	}

	private void update(Path pomFilePath, String dependencyBeforeUpdate, String dependencyAfterUpdate) throws IOException {
		LOG.debug("beginning of update in DependencyUpdateService");
		String fileContent = Files.lines(pomFilePath).collect(Collectors.joining("\n"));
		Pattern pattern = Pattern.compile(dependencyBeforeUpdate);
	    Matcher matcher = pattern.matcher(fileContent);
	    StringBuffer spacesToRetain = new StringBuffer();
		if(matcher.find()) {
			Files.lines(pomFilePath).forEach(line -> {
				if(line.contains("<artifactId>" + dependencyToUpdateFromArtifactId +"</artifactId>")) {
					spacesToRetain.append(line.substring(0, (line.length() - ("<artifactId>" + dependencyToUpdateFromArtifactId +"</artifactId>").length())));
				}
			});
			dependencyAfterUpdate = dependencyAfterUpdate.replaceAll(TABS_TO_ADD, spacesToRetain.toString());
			fileContent = fileContent.replaceAll(dependencyBeforeUpdate.toString(), dependencyAfterUpdate);
			Files.write(pomFilePath, fileContent.getBytes());
			String fileLocationFolderName = pomFilePath.toString().substring(getLocationPath().toString().length() +1);
			fileLocationFolderName = fileLocationFolderName.replace("\\pom.xml", "");
			LOG.info("POM File in "+ fileLocationFolderName + " updated successfully.");
		}
		
		
	}

	private void verifyInputValues() {
		LOG.debug("beginning of verifyInputValues in DependencyUpdateService");
		if (location == null || location.isEmpty())
			throw new RuntimeException("Invalid Location");
		Path locationPath = getLocationPath();
		if (!Files.exists(locationPath) || !Files.isDirectory(locationPath))
			throw new RuntimeException("Invalid Location");

		if (dependencyToUpdateFromGroupId == null || dependencyToUpdateFromGroupId.isEmpty())
			throw new RuntimeException("Depdencency to be updated GROUPID are not valid");

		if (dependencyToUpdateFromArtifactId == null || dependencyToUpdateFromArtifactId.isEmpty())
			throw new RuntimeException("Depdencency to be updated ARTIFACTID are not valid");

		if (dependencyToUpdateToVersion == null || dependencyToUpdateToVersion.isEmpty())
			throw new RuntimeException("Depdencency to be updated TO VERSION details are not valid");

	}

	private Path getLocationPath() {
		LOG.debug("beginning of getLocationPath in DependencyUpdateService");
		if (!location.endsWith("/"))
			location = location.concat("/");
		location = location.replaceAll("\\\\", "/");
		return Paths.get(location);
	}
}
