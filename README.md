# dependency-version-update
Update pom file dependency version


> Any dependency mentioned in the pom file can be updated by retaining all the spaces and tabs without disrubting any allignment.
> Multiple projects of having same dependency version can be updated
> By specifying a parent folder all the projects under it can be updated at a single run


Enter/update below details in the application.properties file

- dependency.update.location= specify root folder location
- dependency.update.from.groupId= specify dependency groupId to be updated
- dependency.update.from.artifactId=specify dependency artifactId to be updated
- dependency.update.from.version= specify dependency from what version to be updated
- dependency.update.to.version= specify dependency version to be updated


## NOTE
dependency.update.from.version is optional if you specify only that specific version is updated else all the versions of that artifact will ve updated
