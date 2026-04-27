package com.prd.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

@Slf4j
@Component
public class BackupConfig {

    private static final String DATA_DIR = System.getProperty("user.home") + "/.personal-relationship/data";
    private static final String BACKUP_DIR = System.getProperty("user.home") + "/.personal-relationship/backup";

    @PostConstruct
    public void init() {
        log.info("数据文件路径: {}", DATA_DIR);
        performDailyBackup();
    }

    private void performDailyBackup() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            return;
        }

        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        String today = DateUtil.today();
        File[] backups = backupDir.listFiles((dir, name) -> name.startsWith("prd_backup_"));

        // 检查今天是否已备份
        if (backups != null) {
            for (File backup : backups) {
                if (backup.getName().contains(today)) {
                    log.info("今日已备份，跳过: {}", backup.getName());
                    return;
                }
            }
        }

        // 执行备份
        String backupName = "prd_backup_" + today + ".zip";
        File backupFile = new File(BACKUP_DIR, backupName);

        try {
            cn.hutool.core.util.ZipUtil.zip(DATA_DIR, backupFile.getAbsolutePath());
            log.info("数据备份完成: {}", backupFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("数据备份失败", e);
        }

        // 清理旧备份，保留最近7天
        if (backups != null && backups.length > 7) {
            Arrays.sort(backups, Comparator.comparingLong(File::lastModified));
            for (int i = 0; i < backups.length - 7; i++) {
                FileUtil.del(backups[i]);
                log.info("清理旧备份: {}", backups[i].getName());
            }
        }
    }
}
