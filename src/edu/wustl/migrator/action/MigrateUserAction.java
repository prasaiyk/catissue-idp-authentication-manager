package edu.wustl.migrator.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.abstractidp.WustlKeyIDP;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.LDAPAuthManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.IAbstractMigrator;
import edu.wustl.migrator.WUSTLKeyMigrator;
import edu.wustl.migrator.actionform.MigrationForm;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class performs the migration of a user from one domain to the other, by
 * calling the migrate method defined in the appropriate migrator class.
 *
 * @author niharika_sharma
 *
 */
public class MigrateUserAction extends AbstractMigrationAction
{

    /** The Constant LOGGER. */
    private static final org.apache.log4j.Logger LOGGER = LoggerConfig
            .getConfiguredLogger(NeverMigrateUserAction.class);

    /*
     * (non-Javadoc)
     *
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception
    {
        final MigrationForm loginForm = (MigrationForm) form;
        boolean loginOK = false;
        final String flag = (String) request.getAttribute(Constants.PAGE_OF_WUSTL_CONNECT);
        if (flag != null && Constants.TRUE.equals(flag))
        {
            return mapping.findForward(Constants.SUCCESS);
        }
        final SessionDataBean sessionDataBean = getSessionData(request);

        final IAbstractMigrator migrator = new WUSTLKeyMigrator(new WustlKeyIDP());
        try
        {
            final IDPAuthManager authManager = new LDAPAuthManager();
            loginOK = authManager.authenticate(loginForm.getMigratedLoginName(), loginForm.getMigratedPassword());
            if (!loginOK)
            {
                handleError(request, "errors.wustlkeyorpassword");
                return mapping.findForward(Constants.FAILURE);
            }
            else
            {
                final UserDetails userDetails = new UserDetails();
                userDetails.setLoginName(sessionDataBean.getUserName());
                userDetails.setMigratedLoginName(loginForm.getMigratedLoginName());
                migrator.migrate(userDetails);
                handleCustomMessage(request);
                return mapping.findForward(Constants.LOGIN);
            }
        }
        catch (final Exception e)
        {
            LOGGER.info("Exception: " + e.getMessage(), e);
            handleError(request, "errors.wustlkeyorpassword");
            return mapping.findForward(Constants.FAILURE);
        }
    }

}