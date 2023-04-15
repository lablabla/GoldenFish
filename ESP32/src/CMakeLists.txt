# This file was automatically generated for projects
# without default 'CMakeLists.txt' file.


idf_component_register(
		SRCS main.c blufi.c blufi_security.c
		INCLUDE_DIRS ".")

target_add_binary_data(${COMPONENT_TARGET} "server_root_cert.pem" TEXT)
