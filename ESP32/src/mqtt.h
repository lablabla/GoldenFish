#pragma once

#include "esp_event.h"

#define NVS_KEY_MQTT_BROKER_ADDRESS "mqtt_address"
#define NVS_KEY_MQTT_USERNAME "mqtt_username"
#define NVS_KEY_MQTT_PASSWORD "mqtt_password"

void mqtt_app_start(const char* broker_address, const char* username, const char* password);
void mqtt_event_handler(void *handler_args, esp_event_base_t base, int32_t event_id, void *event_data);
bool try_load_mqtt_config(char** broker_address, char** username, char** password);
bool try_save_mqtt_config(char* broker_address, char* username, char* password);