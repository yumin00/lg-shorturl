insert into Member (id, name)
    values (1, 'First');
insert into Member (id, name)
    values (2, 'Second');
insert into Url_Mapping (id, original_url, short_url)
    values (1, 'https://www.example.com', 'example');
insert into Url_Mapping (id, original_url, short_url)
    values (2, 'test', 't');

-- 1시간 전
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP()));

-- 2시간 전
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -2, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -2, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -2, CURRENT_TIMESTAMP()));

-- 4시간 전
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP()));

-- 12시간 전
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -12, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -12, CURRENT_TIMESTAMP()));

-- 18시간 전
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -18, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -18, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -18, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -18, CURRENT_TIMESTAMP()));

-- 23시간 전
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP()));

-- 두번째 URL에 대한 접근 로그 (URL ID 2)
insert into Url_Access_log (url_mapping_id, accessed_at) values (2, DATEADD('HOUR', -3, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (2, DATEADD('HOUR', -3, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (2, DATEADD('HOUR', -6, CURRENT_TIMESTAMP()));
insert into Url_Access_log (url_mapping_id, accessed_at) values (2, DATEADD('HOUR', -10, CURRENT_TIMESTAMP()));