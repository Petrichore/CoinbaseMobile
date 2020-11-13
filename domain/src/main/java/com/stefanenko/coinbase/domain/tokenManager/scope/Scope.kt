package com.stefanenko.coinbase.domain.tokenManager.scope

object Scope {
    interface Wallet {
        interface Withdrawals {
            companion object {
                const val READ = "wallet:withdrawals:read"
                const val CREATE = "wallet:withdrawals:create"
            }
        }

        interface User {
            companion object {
                const val READ = "wallet:user:read"
                const val UPDATE = "wallet:user:update"
                const val EMAIL = "wallet:user:email"
            }
        }

        interface Transactions {
            companion object {
                const val READ = "wallet:transactions:read"
                const val SEND = "wallet:transactions:send"
                const val SEND_BYPASS_2FA = "wallet:transactions:send:bypass-2fa"
                const val REQUEST = "wallet:transactions:request"
                const val TRANSFER = "wallet:transactions:transfer"
            }
        }

        interface Sells {
            companion object {
                const val READ = "wallet:sells:read"
                const val CREATE = "wallet:sells:create"
            }
        }

        interface PaymentMethods {
            companion object {
                const val READ = "wallet:payment-methods:read"
                const val DELETE = "wallet:payment-methods:delete"
                const val LIMITS = "wallet:payment-methods:limits"
            }
        }

        interface Orders {
            companion object {
                const val READ = "wallet:orders:read"
                const val CREATE = "wallet:orders:create"
                const val REFUND = "wallet:orders:refund"
            }
        }

        interface Deposits {
            companion object {
                const val READ = "wallet:deposits:read"
                const val CREATE = "wallet:deposits:create"
            }
        }

        interface Checkouts {
            companion object {
                const val READ = "wallet:checkouts:read"
                const val CREATE = "wallet:checkouts:create"
            }
        }

        interface Buys {
            companion object {
                const val READ = "wallet:buys:read"
                const val CREATE = "wallet:buys:create"
            }
        }

        interface Addresses {
            companion object {
                const val READ = "wallet:addresses:read"
                const val CREATE = "wallet:addresses:create"
            }
        }

        interface Accounts {
            companion object {
                const val READ = "wallet:accounts:read"
                const val UPDATE = "wallet:accounts:update"
                const val CREATE = "wallet:accounts:create"
                const val DELETE = "wallet:accounts:delete"
            }
        }
    }
}