CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS subject;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS professor_subject;
DROP TABLE IF EXISTS lecture;
DROP TABLE IF EXISTS professor;

CREATE TABLE room (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL
);

CREATE TABLE professor (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE subject (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    code_class VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE professor_subject (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    professor_id UUID REFERENCES professor(id) ON DELETE CASCADE,
    subject_id UUID REFERENCES subject(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE lecture (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subject_id UUID REFERENCES subject(id) ON DELETE CASCADE,
    room_id UUID REFERENCES room(id) ON DELETE SET NULL,
    day_of_week VARCHAR(255) NOT NULL,
    hour_init TIMESTAMP WITH TIME ZONE NOT NULL,
    duration INTEGER NOT NULL
);