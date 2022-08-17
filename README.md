# dependency-update
Update pom file dependency version


> Any dependency mentioned in the pom file can be updated by retaining all the spaces and tabs without disrubting any allignment.
> Multiple projects of having same dependency version can be updated
> By specifying a parent folder all the projects under it can be updated at a single run


Enter/update below details in the application.properties file

- dependency.update.location=<root folder location>
- dependency.update.from.groupId=<dependency-group-id>
- dependency.update.from.artifactId=<dependency-artifact-id>
- dependency.update.from.version=<dependency-old-version>
- dependency.update.to.version=<dependency-new-version>


## NOTE
dependency.update.from.version is optional if you specify only that specific version is updated else all the versions of that artifact will ve updated
