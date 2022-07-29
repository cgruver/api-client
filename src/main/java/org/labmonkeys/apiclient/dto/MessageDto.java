package org.labmonkeys.apiclient.dto;

import java.util.UUID;

public record MessageDto(UUID messageId, String message){}