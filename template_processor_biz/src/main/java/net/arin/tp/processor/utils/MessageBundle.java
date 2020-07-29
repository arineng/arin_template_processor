package net.arin.tp.processor.utils;

/**
 * These should map to the template_validation_messages.properties file. This should allow us to use the IDE
 * searching/auto-complete features to find message properties.
 */
public enum MessageBundle
{
    TEMPLATE_FIELD_LIMIT_EXCEEDED,
    TEMPLATE_INVALID_ACTION,
    MISSING_API_KEY,
    MULTIPLE_API_KEY,
    AMBIGUOUS_VALUES,
    UNKNOWN_ERROR,
    UNKNOWN_TEMPLATE_ACTION_MESSAGE,
    NET_MODIFY_SIMPLE_REASSIGN,
    SIMPLE_REASSIGN_INVALID_MODIFY,
    IP_ADDRESS_REQUIRED,
    IPV4_ADDRESS_FORMAT,
    IPV6_ADDRESS_FORMAT,
    MISSING_REQUIRED_ELEMENT,
    NET_MOD_TEMPLATE_INVALID_REMOVE,
    NET_MOD_TEMPLATE_MULTIPLE_NETS_FOUND
}
