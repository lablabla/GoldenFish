
#include <inttypes.h>

#include "mqtt.h"

#include "esp_log.h"
#include "nvs_flash.h"
#include "mqtt_client.h"

static const char *TAG = "GOLDEN_FISH_MQTT";

extern const uint8_t server_root_cert_pem_start[] asm("_binary_server_root_cert_pem_start");
extern const uint8_t server_root_cert_pem_end[] asm("_binary_server_root_cert_pem_end");

void mqtt_app_start(const char* broker_address, const char* username, const char* password)
{
    const esp_mqtt_client_config_t mqtt_cfg = {
        .broker = {
            .address.uri = broker_address,
            .verification.certificate = (const char *)server_root_cert_pem_start
        },
        .credentials = {
            .username = username,
            .authentication = {
                .password = password
            }
        }
    };

    ESP_LOGI(TAG, "[APP] Free memory: %" PRIu32 " bytes", esp_get_free_heap_size());
    esp_mqtt_client_handle_t client = esp_mqtt_client_init(&mqtt_cfg);
    /* The last argument may be used to pass data to the event handler, in this example mqtt_event_handler */
    esp_mqtt_client_register_event(client, ESP_EVENT_ANY_ID, mqtt_event_handler, NULL);
    esp_mqtt_client_start(client);
}

/*
 * @brief Event handler registered to receive MQTT events
 *
 *  This function is called by the MQTT client event loop.
 *
 * @param handler_args user data registered to the event.
 * @param base Event base for the handler(always MQTT Base in this example).
 * @param event_id The id for the received event.
 * @param event_data The data for the event, esp_mqtt_event_handle_t.
 */
void mqtt_event_handler(void *handler_args, esp_event_base_t base, int32_t event_id, void *event_data)
{
    ESP_LOGD(TAG, "Event dispatched from event loop base=%s, event_id=%" PRIi32, base, event_id);
    esp_mqtt_event_handle_t event = event_data;
    esp_mqtt_client_handle_t client = event->client;
    int msg_id;
    switch ((esp_mqtt_event_id_t)event_id) {
    case MQTT_EVENT_CONNECTED:
        ESP_LOGI(TAG, "MQTT_EVENT_CONNECTED");
        msg_id = esp_mqtt_client_subscribe(client, "/topic/qos0", 0);
        ESP_LOGI(TAG, "sent subscribe successful, msg_id=%d", msg_id);

        msg_id = esp_mqtt_client_subscribe(client, "/topic/qos1", 1);
        ESP_LOGI(TAG, "sent subscribe successful, msg_id=%d", msg_id);

        msg_id = esp_mqtt_client_unsubscribe(client, "/topic/qos1");
        ESP_LOGI(TAG, "sent unsubscribe successful, msg_id=%d", msg_id);
        break;
    case MQTT_EVENT_DISCONNECTED:
        ESP_LOGI(TAG, "MQTT_EVENT_DISCONNECTED");
        break;

    case MQTT_EVENT_SUBSCRIBED:
        ESP_LOGI(TAG, "MQTT_EVENT_SUBSCRIBED, msg_id=%d", event->msg_id);
        msg_id = esp_mqtt_client_publish(client, "/topic/qos0", "data", 0, 0, 0);
        ESP_LOGI(TAG, "sent publish successful, msg_id=%d", msg_id);
        break;
    case MQTT_EVENT_UNSUBSCRIBED:
        ESP_LOGI(TAG, "MQTT_EVENT_UNSUBSCRIBED, msg_id=%d", event->msg_id);
        break;
    case MQTT_EVENT_PUBLISHED:
        ESP_LOGI(TAG, "MQTT_EVENT_PUBLISHED, msg_id=%d", event->msg_id);
        break;
    case MQTT_EVENT_DATA:
        ESP_LOGI(TAG, "MQTT_EVENT_DATA");
        printf("TOPIC=%.*s\r\n", event->topic_len, event->topic);
        printf("DATA=%.*s\r\n", event->data_len, event->data);
        break;
    case MQTT_EVENT_ERROR:
        ESP_LOGI(TAG, "MQTT_EVENT_ERROR");
        if (event->error_handle->error_type == MQTT_ERROR_TYPE_TCP_TRANSPORT) {
            ESP_LOGI(TAG, "Last error code reported from esp-tls: 0x%x", event->error_handle->esp_tls_last_esp_err);
            ESP_LOGI(TAG, "Last tls stack error number: 0x%x", event->error_handle->esp_tls_stack_err);
            ESP_LOGI(TAG, "Last captured errno : %d (%s)",  event->error_handle->esp_transport_sock_errno,
                     strerror(event->error_handle->esp_transport_sock_errno));
        } else if (event->error_handle->error_type == MQTT_ERROR_TYPE_CONNECTION_REFUSED) {
            ESP_LOGI(TAG, "Connection refused error: 0x%x", event->error_handle->connect_return_code);
        } else {
            ESP_LOGW(TAG, "Unknown error type: 0x%x", event->error_handle->error_type);
        }
        break;
    default:
        ESP_LOGI(TAG, "Other event id:%d", event->event_id);
        break;
    }
}

bool try_load_mqtt_config(char** broker_address, char** username, char** password)
{
    ESP_LOGI(TAG, "try_load_mqtt_config:");
    ESP_LOGI(TAG, "Opening NVS");
    nvs_handle_t nvs_handle;
    esp_err_t err = nvs_open("nvs", NVS_READWRITE, &nvs_handle);
    if (err != ESP_OK) 
    {
        ESP_LOGE(TAG, "Failed opening NVS partition (%d: %s)", err, esp_err_to_name(err));
        return false;
    }
    ESP_LOGI(TAG, "Opened NVS partition");

    // Try read broker address
    size_t brokerAddressLength = 0; 
    err = nvs_get_str(nvs_handle, NVS_KEY_MQTT_BROKER_ADDRESS, NULL, &brokerAddressLength);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed reading length for key %s (%d: %s)", NVS_KEY_MQTT_BROKER_ADDRESS, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        return false;
    }
    ESP_LOGI(TAG, "Length for key %s: %" PRIu16, NVS_KEY_MQTT_BROKER_ADDRESS, brokerAddressLength);

    size_t usernameLength = 0; 
    err = nvs_get_str(nvs_handle, NVS_KEY_MQTT_USERNAME, NULL, &usernameLength);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed reading length for key %s (%d: %s)", NVS_KEY_MQTT_USERNAME, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        return false;
    }
    ESP_LOGI(TAG, "Length for key %s: %" PRIu16, NVS_KEY_MQTT_USERNAME, usernameLength);

    size_t passwordLength = 0; 
    err = nvs_get_str(nvs_handle, NVS_KEY_MQTT_PASSWORD, NULL, &passwordLength);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed reading length for key %s (%d: %s)", NVS_KEY_MQTT_PASSWORD, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        return false;
    }
    ESP_LOGI(TAG, "Length for key %s: %" PRIu16, NVS_KEY_MQTT_PASSWORD, passwordLength);

    // If we reached here we know we can read all 3 values and we have their sizes
    *broker_address = malloc(brokerAddressLength);
    *username = malloc(usernameLength);
    *password = malloc(passwordLength);

    err = nvs_get_str(nvs_handle, NVS_KEY_MQTT_BROKER_ADDRESS, *broker_address, &brokerAddressLength);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed reading value for key %s (%d: %s)", NVS_KEY_MQTT_BROKER_ADDRESS, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        free(*broker_address);
        free(*username);
        free(*password);
        return false;
    }

    err = nvs_get_str(nvs_handle, NVS_KEY_MQTT_USERNAME, *username, &usernameLength);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed reading value for key %s (%d: %s)", NVS_KEY_MQTT_USERNAME, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        free(*broker_address);
        free(*username);
        free(*password);
        return false;
    }
    
    err = nvs_get_str(nvs_handle, NVS_KEY_MQTT_PASSWORD, *password, &passwordLength);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed reading length for key %s (%d: %s)", NVS_KEY_MQTT_PASSWORD, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        free(*broker_address);
        free(*username);
        free(*password);
        return false;
    }

    // Close NVS
    nvs_close(nvs_handle);
    return true;
}

bool try_save_mqtt_config(char* broker_address, char* username, char* password)
{
    ESP_LOGI(TAG, "try_save_mqtt_config:");
    ESP_LOGI(TAG, "Opening NVS");
    nvs_handle_t nvs_handle;
    esp_err_t err = nvs_open("nvs", NVS_READWRITE, &nvs_handle);
    if (err != ESP_OK) 
    {
        ESP_LOGE(TAG, "Failed opening NVS partition (%d: %s)", err, esp_err_to_name(err));
        return false;
    }
    ESP_LOGI(TAG, "Opened NVS partition");

    err = nvs_set_str(nvs_handle, NVS_KEY_MQTT_BROKER_ADDRESS, broker_address);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed writing value key %s (%d: %s)", NVS_KEY_MQTT_BROKER_ADDRESS, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        return false;
    }

    err = nvs_set_str(nvs_handle, NVS_KEY_MQTT_USERNAME, username);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed writing value key %s (%d: %s)", NVS_KEY_MQTT_USERNAME, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        return false;
    }

    err = nvs_set_str(nvs_handle, NVS_KEY_MQTT_PASSWORD, password);
    if (err != ESP_OK)
    {
        ESP_LOGE(TAG, "Failed writing value key %s (%d: %s)", NVS_KEY_MQTT_PASSWORD, err, esp_err_to_name(err));
        nvs_close(nvs_handle);
        return false;
    }

    ESP_LOGI(TAG, "Done saving MQTT info to NVS");
    // Close NVS
    nvs_close(nvs_handle);
    return true;
}