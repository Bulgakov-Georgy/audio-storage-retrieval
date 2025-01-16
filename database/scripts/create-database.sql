create role audio_storage_retrieval with login password 'audio_storage_retrieval';
create database audio_storage_retrieval owner audio_storage_retrieval encoding 'UTF-8';
create schema if not exists audio_storage_retrieval authorization audio_storage_retrieval;