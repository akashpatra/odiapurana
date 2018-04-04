package in.co.trapps.odiapurana.logger;

/**
 * Enum Class which will enable/disable printing of logs.
 * It implements {@link ILoggerActivator}
 *
 * @author Akash Patra
 */
public enum LoggerEnable implements ILoggerActivator {
    MainAct {
        @Override
        public boolean isEnabled() {
            return true;
        }
    },
    MusicFragment {
        @Override
        public boolean isEnabled() {
            return false;
        }
    },
    MusicServiceFragment {
        @Override
        public boolean isEnabled() {
            return false;
        }
    },
    MusicService {
        @Override
        public boolean isEnabled() {
            return false;
        }
    },
    LaxmiPuranaFragment {
        @Override
        public boolean isEnabled() {
            return false;
        }
    },
    LaxmiPuranaVideoFragment {
        @Override
        public boolean isEnabled() {
            return false;
        }
    },
    MySharedPrefs {
        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
