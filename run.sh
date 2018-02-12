java \
	-Djava.library.path=. \
	-Dcom.amd.aparapi.executionMode=%1 \
	-classpath ./aparapi.jar:VisionTracking.jar \
	controller.Runner
