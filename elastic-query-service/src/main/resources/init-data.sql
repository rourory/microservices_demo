CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


INSERT INTO public.users(
    id, username, firstname, lastname)
VALUES ('56a0a1c4-3163-4d55-be71-35fd21221e08', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
    id, username, firstname, lastname)
VALUES ('bd5a40c8-29e8-4b68-bbc6-89bc8e65fa08', 'app_admin', 'Standard', 'Admin');
INSERT INTO public.users(
    id, username, firstname, lastname)
VALUES ('b3c5ace3-4f11-4dd8-82c7-a9b7dcee8883', 'app_superuser', 'Standard', 'Superuser');


insert into documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 1);
insert into documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', 2);
insert into documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', 3);

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'56a0a1c4-3163-4d55-be71-35fd21221e08', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'bd5a40c8-29e8-4b68-bbc6-89bc8e65fa08', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'bd5a40c8-29e8-4b68-bbc6-89bc8e65fa08', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), 'bd5a40c8-29e8-4b68-bbc6-89bc8e65fa08', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), 'b3c5ace3-4f11-4dd8-82c7-a9b7dcee8883', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');


