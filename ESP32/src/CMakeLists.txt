# This file was automatically generated for projects
# without default 'CMakeLists.txt' file.

set(SRCS 
	main.c
	blufi.c
	blufi_security.c
	mqtt.c
)

idf_component_register(
		SRCS ${SRCS}
		INCLUDE_DIRS ".")

target_add_binary_data(${COMPONENT_TARGET} "server_root_cert.pem" TEXT)
