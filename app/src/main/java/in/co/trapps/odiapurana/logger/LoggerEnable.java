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
            return false;
        }
    },
    MusicFragment {
        @Override
        public boolean isEnabled() {
            return true;
        }
    },
    MusicServiceFragment {
        @Override
        public boolean isEnabled() {
            return true;
        }
    },
    MusicService {
        @Override
        public boolean isEnabled() {
            return true;
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
    BeatPlanFragment {
        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
