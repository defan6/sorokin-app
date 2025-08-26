CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date TIMESTAMP NOT NULL,

    venue_id BIGINT NOT NULL,
    organizer_id BIGINT NOT NULL,

    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venues (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_organizer FOREIGN KEY (organizer_id) REFERENCES users (id) ON DELETE CASCADE
);